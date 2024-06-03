package net.khaibq.springbootstater.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import net.khaibq.springbootstater.dto.user.UserDto;
import net.khaibq.springbootstater.dto.user.UserSearchDto;
import net.khaibq.springbootstater.repository.CustomUserRepository;
import net.khaibq.springbootstater.utils.DbUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository {
    private final EntityManager entityManager;
    private final DbUtils dbUtils;

    @Override
    public Page<UserDto> getPageUser(UserSearchDto dto, Pageable pageable) {
        Session session = entityManager.unwrap(Session.class);

        String sql = """
                select u.id usId, u.username usUsername, u.email usEmail, u.role usRole from tbl_user u
                where 1=1
                """;
        Map<String, Object> params = new HashMap<>();

        if (dto.getUsername() != null) {
            sql += " and u.username = :username";
            params.put("username", dto.getUsername());
        }
        if (dto.getEmail() != null) {
            sql += " and u.email = :email";
            params.put("email", dto.getEmail());
        }

        String countSql = "select count(*) from (" + sql + ")";
        Query<Tuple> query = dbUtils.buildQuery(session, sql, params);

        Query<Long> countQuery = dbUtils.buildCountQuery(session, countSql, params);
        Long total = countQuery.getSingleResult();

        List<UserDto> list = query
                .setTupleTransformer((objects, strings) -> dbUtils.transformData(objects, strings, UserDto.class))
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(list, pageable, total);
    }
}
