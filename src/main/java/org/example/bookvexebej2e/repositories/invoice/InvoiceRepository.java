package org.example.bookvexebej2e.repositories.invoice;


import org.example.bookvexebej2e.models.db.InvoiceDbModel;
import org.example.bookvexebej2e.repositories.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends BaseRepository<InvoiceDbModel> {
}
