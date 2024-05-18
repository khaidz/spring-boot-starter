package net.khaibq.springbootstater.utils;

import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DbUtils {
    private final ModelMapper modelMapper;

    public Query<Tuple> buildQuery(Session session, String sql, Map<String, Object> params) {
        Query<Tuple> query = session.createNativeQuery(sql, Tuple.class);
        for (String key : params.keySet()) {
            query.setParameter(key, params.get(key));
        }
        return query;
    }

    public <T> T transformData(Object[] values, String[] fields, Class<T> targetType) {
        Map<String, Object> data = new HashMap<>();
        for (int i = 0; i < fields.length; i++) {
            if (values[i] instanceof Timestamp timestamp) {
                data.put(fields[i], timestamp.toLocalDateTime());
            } else {
                data.put(fields[i], values[i]);
            }
        }
        return modelMapper.map(data, targetType);
    }
}
