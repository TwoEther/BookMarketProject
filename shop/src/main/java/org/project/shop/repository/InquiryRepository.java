package org.project.shop.repository;

import org.project.shop.domain.Inquiry;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository {
    public void save(Inquiry inquiry);

    public Inquiry findById(Long id);

    public List<Inquiry> findByItemId(Long id);


    public List<Inquiry> findAllInquiry();
    public void delete(Long id);
}
