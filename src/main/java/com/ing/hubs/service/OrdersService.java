package com.ing.hubs.service;

import com.ing.hubs.model.dto.CreateOrderDto;
import com.ing.hubs.model.dto.OrderFilter;
import com.ing.hubs.model.dto.Side;
import com.ing.hubs.model.dto.Status;
import com.ing.hubs.model.entity.Orders;
import com.ing.hubs.repository.AssetRepository;
import com.ing.hubs.repository.OrdersRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdersService {

    private final OrdersRepository repository;
    private final AssetRepository assetRepository;
    private final EntityManager entityManager;
    private final CustomerService service;

    @Transactional
    public Orders createOrder(CreateOrderDto dto) {
        var asset = assetRepository.findByCustomerIdAndAssetName(dto.getCustomer(), dto.getAsset())
                .orElseThrow(() -> new RuntimeException("No asset found with customer: " + dto.getCustomer() +
                        " and asset: " + dto.getAsset()));
        if (Side.BUY.equals(dto.getSide()) && asset.getUsableSize() < dto.getSize() * dto.getPrice()) {
            throw new RuntimeException("Insufficient funds!");
        }
        var order = new Orders();
        CreateOrderDto.mapToEntity(dto, order);
        if (Side.BUY.equals(order.getOrderSide())) {
            asset.setUsableSize(asset.getUsableSize() - order.getSize() * order.getPrice());
        } else {
            asset.setUsableSize(asset.getUsableSize() + order.getSize() * order.getPrice());
        }
        assetRepository.save(asset);
        order.setCreateDate(LocalDateTime.now());
        order.setStatus(Status.PENDING);
        return repository.save(order);
    }

    public List<Orders> getOrders(OrderFilter filter) {
        var cb = entityManager.getCriteriaBuilder();
        var cq = cb.createQuery(Orders.class);
        var ordersRoot = cq.from(Orders.class);
        var predicates = new ArrayList<Predicate>();
        if (filter.getCustomerId() != null) {
            predicates.add(cb.equal(ordersRoot.get("customerId"), filter.getCustomerId()));
        }
        if (filter.getAsset() != null) {
            predicates.add(cb.equal(ordersRoot.get("assetName"), filter.getAsset()));
        }
        if (filter.getAfterDate() != null) {
            predicates.add(cb.greaterThan(ordersRoot.get("createDate"), filter.getAfterDate()));
        }
        if (filter.getBeforeDate() != null) {
            predicates.add(cb.lessThan(ordersRoot.get("createDate"), filter.getBeforeDate()));
        }

        if (!predicates.isEmpty()) {
            cq.where(cb.and(predicates.toArray(new Predicate[0])));
        }

        var query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    @Transactional
    public Orders deleteOrder(int orderId) {
        var order = repository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("No order found with given id: " + orderId));

        service.authorizeCustomer(order.getCustomerId());

        if (!Status.PENDING.equals(order.getStatus())) {
            throw new RuntimeException("Only pending orders can be deleted");
        }
        var asset = assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), order.getAssetName())
                .orElseThrow(() -> new RuntimeException("No asset found for given order. orderId: " + orderId));

        if (Side.BUY.equals(order.getOrderSide())) {
            asset.setUsableSize(asset.getUsableSize() + order.getSize() * order.getPrice());
        } else {
            asset.setUsableSize(asset.getUsableSize() - order.getSize() * order.getPrice());
        }

        assetRepository.save(asset);
        order.setStatus(Status.CANCELED);
        return repository.save(order);
    }

    public Orders matchOrder(Integer orderId) {
        var order = repository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("No order found with given id: " + orderId));

        if (!Status.PENDING.equals(order.getStatus())) {
            throw new RuntimeException("Only pending orders can be deleted");
        }
        var asset = assetRepository.findByCustomerIdAndAssetName(order.getCustomerId(), order.getAssetName())
                .orElseThrow(() -> new RuntimeException("No asset found for given order. orderId: " + orderId));

        if (Side.BUY.equals(order.getOrderSide())) {
            asset.setSize(asset.getSize() + order.getSize());
            asset.setUsableSize(asset.getUsableSize() + order.getSize());
        } else {
            asset.setSize(asset.getSize() - order.getSize());
            asset.setUsableSize(asset.getUsableSize() - order.getSize());
        }

        assetRepository.save(asset);
        order.setStatus(Status.MATCHED);
        return repository.save(order);
    }
}
