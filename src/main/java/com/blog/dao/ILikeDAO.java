package com.blog.dao;

import com.blog.model.entity.Like;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

public interface ILikeDAO extends PanacheRepository<Like> {
}
