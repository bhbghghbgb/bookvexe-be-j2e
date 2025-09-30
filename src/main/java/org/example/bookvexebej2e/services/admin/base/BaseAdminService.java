package org.example.bookvexebej2e.services.admin.base;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public abstract class BaseAdminService<T, ID> {

    protected abstract JpaRepository<T, ID> getRepository();

    public Optional<T> findById(ID id) {
        return getRepository().findById(id);
    }

    public List<T> findAll() {
        return getRepository().findAll();
    }

    public Page<T> findAll(Pageable pageable) {
        return getRepository().findAll(pageable);
    }

    public boolean existsById(ID id) {
        return getRepository().existsById(id);
    }
}
