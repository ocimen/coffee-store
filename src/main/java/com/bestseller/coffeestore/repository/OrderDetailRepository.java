package com.bestseller.coffeestore.repository;

import com.bestseller.coffeestore.dto.ToppingUsageDto;
import com.bestseller.coffeestore.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    @Query(value = """
    select  new com.bestseller.coffeestore.dto.ToppingUsageDto(
            count(*) as usageCount, od.productName as toppingName)
            from OrderDetail as od
            where od.productType = 'topping'
            group by od.productName
            order by usageCount desc
""")
    List<ToppingUsageDto> getToppingUsage();
}
