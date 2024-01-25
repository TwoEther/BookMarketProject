package org.project.shop.repository;

import org.project.shop.domain.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository {
    public void save(Inquiry inquiry);

    public Inquiry findById(Long id);

    public Page<Inquiry> findByItemId(PageRequest pageRequest, Long id);


    public List<Inquiry> findAllInquiryByGeneralMember();
    public void delete(Long id);
}
