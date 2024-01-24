package org.shlimtech.typesevendatabasecommon.repository;

import org.shlimtech.typesevendatabasecommon.model.Type7Metadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Type7MetadataRepository extends JpaRepository<Type7Metadata, Integer> {

    Type7Metadata findByUserId(int id);

}
