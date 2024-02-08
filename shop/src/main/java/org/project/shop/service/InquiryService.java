package org.project.shop.service;

import org.project.shop.domain.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface InquiryService {
    public void save(Inquiry inquiry);

    public Inquiry findById(Long id);

    public Page<Inquiry> findByItemId(PageRequest pageRequest, Long id);

    public List<Inquiry> findAllInquiryByGeneralMember();

    public void deleteAll();

    public void delete(Long id);
}
