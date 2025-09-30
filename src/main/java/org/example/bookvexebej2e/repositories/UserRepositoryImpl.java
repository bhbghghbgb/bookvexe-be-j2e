package org.example.bookvexebej2e.repositories;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.bookvexebej2e.models.db.QRoleUserDbModel;
import org.example.bookvexebej2e.models.db.QUserDbModel;
import org.example.bookvexebej2e.models.db.UserDbModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<UserDbModel> findUsersByCriteria(String name, String email, Boolean active, Pageable pageable) {
        QUserDbModel user = QUserDbModel.userDbModel;

        BooleanBuilder predicate = new BooleanBuilder();

        if (StringUtils.hasText(name)) {
            predicate.and(user.fullName.containsIgnoreCase(name));
        }
        if (StringUtils.hasText(email)) {
            predicate.and(user.email.containsIgnoreCase(email));
        }
        if (active != null) {
            predicate.and(user.isActive.eq(active));
        }

        JPAQuery<UserDbModel> query = queryFactory.selectFrom(user)
            .where(predicate);

        long total = query.fetchCount();
        List<UserDbModel> content = query.offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(getOrderSpecifier(user, pageable))
            .fetch();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<UserDbModel> findUsersWithActiveRoles() {
        QUserDbModel user = QUserDbModel.userDbModel;
        QRoleUserDbModel roleUser = QRoleUserDbModel.roleUserDbModel;

        return queryFactory.selectFrom(user)
            .leftJoin(user.roles, roleUser)
            .on(roleUser.isActive.eq(true))
            .where(user.isActive.eq(true))
            .distinct()
            .fetch();
    }

    private OrderSpecifier<?>[] getOrderSpecifier(QUserDbModel user, Pageable pageable) {
        return pageable.getSort()
            .stream()
            .map(order -> {
                Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                return switch (order.getProperty()) {
                    case "fullName" -> new OrderSpecifier<>(direction, user.fullName);
                    case "email" -> new OrderSpecifier<>(direction, user.email);
                    case "createdAt" -> new OrderSpecifier<>(direction, user.createdAt);
                    default -> new OrderSpecifier<>(direction, user.userId);
                };
            })
            .toArray(OrderSpecifier[]::new);
    }
}