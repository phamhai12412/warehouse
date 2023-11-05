package ra.mapper;

import ra.exception.*;

public interface IGenericMapper<T, K, L> {

    T toEntity(K k);

    L toResponse(T t);

}