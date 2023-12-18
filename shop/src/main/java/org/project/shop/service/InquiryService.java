package org.project.shop.service;

import org.project.shop.domain.Inquiry;

import java.util.List;

public interface InquiryService {
    public void save(Inquiry inquiry);

    public Inquiry findById(Long id);

    public List<Inquiry> findAllInquiry();

    public void delete(Long id);
}
