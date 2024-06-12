/*
 * @Author : Thant Htoo Aung
 * @Date : 6/12/2024
 * @Time : 10:10 PM
 * @Project_Name : Spring Boot Auth
 */

package com.vinnnm.springbootJWTAuth.util;

import com.vinnnm.springbootJWTAuth.exception.EntityCreationException;
import com.vinnnm.springbootJWTAuth.exception.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Utility class for common entity operations.
 */
public class EntityUtil {

    /**
     * Saves an entity using the provided repository and checks if the entity was successfully created.
     *
     * Example Usage
     * <pre>
     * {@code
     * User user = new User();
     * user.setName("John Doe");
     * EntityUtil.saveEntity(userRepository, user, "User");
     * }
     * </pre>
     *
     * @param repository the repository for the entity
     * @param entity     the entity to be saved
     * @param entityName the name of the entity
     * @param <T>        the type of the entity
     */
    public static <T> void saveEntity(JpaRepository<T, Long> repository, T entity, String entityName) {
        T savedEntity = repository.save(entity);
        if (savedEntity instanceof Identifiable && ((Identifiable) savedEntity).getId() == null) {
            throw new EntityCreationException("Failed to create the " + entityName);
        }
    }

    /**
     * Retrieves all entities from the repository.
     *
     * Example Usage:
     * <pre>
     * {@code
     * List<User> users = EntityUtil.getAllEntities(userRepository);
     * }
     * </pre>
     *
     * @param repository the repository for the entities
     * @param <T>        the type of the entities
     * @return a list of all entities
     */
    public static <T> List<T> getAllEntities(JpaRepository<T, Long> repository) {
        List<T> entities = repository.findAll();
        if (entities.isEmpty()) {
            return null;
        }
        return entities;
    }

    /**
     * Retrieves an entity by its ID from the repository and throws an exception if not found.
     *
     * Example Usage:
     * <pre>
     * {@code
     * User user = EntityUtil.getEntityById(userRepository, userDto.getId(), "User");
     * }
     * </pre>
     *
     * @param repository the repository for the entity
     * @param id         the ID of the entity
     * @param entityName the name of the entity
     * @param <T>        the type of the entity
     * @return the retrieved entity
     */
    public static <T> T getEntityById(JpaRepository<T, Long> repository, Long id, String entityName) {
        T entity = id > 0 ? repository.findById(id).orElse(null) : null;
        if (entity == null) {
            throw new EntityNotFoundException(entityName + " not found with ID: " + id);
        }
        return entity;
    }

    /**
     * Deletes an entity by its ID from the repository and throws an exception if not found.
     *
     * Example Usage:
     * <pre>
     * {@code
     * EntityUtil.deleteEntity(userRepository, userDto.getId(), "User");
     * }
     * </pre>
     *
     * @param repository the repository for the entity
     * @param id         the ID of the entity to delete
     * @param entityName the name of the entity
     * @param <T>        the type of the entity
     */
    public static <T> void deleteEntity(JpaRepository<T, Long> repository, Long id, String entityName) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException(entityName + " not found");
        }
        repository.deleteById(id);
    }

    /**
     * Interface for entities that are identifiable by an ID.
     */
    public interface Identifiable {
        Long getId();
    }
}
