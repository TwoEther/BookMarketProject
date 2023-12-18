package org.project.shop.service;

import org.project.shop.domain.Inquiry;
import org.project.shop.repository.InquiryRepository;
import org.project.shop.repository.InquiryRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InquiryServiceImpl implements InquiryService{
    private final InquiryRepositoryImpl inquiryRepositoryImpl;

    public InquiryServiceImpl(InquiryRepositoryImpl inquiryRepositoryImpl) {
        this.inquiryRepositoryImpl = inquiryRepositoryImpl;
    }

    @Override
    public void save(Inquiry inquiry) {
        inquiryRepositoryImpl.save(inquiry);
    }

    @Override
    public Inquiry findById(Long id) {
        return inquiryRepositoryImpl.findById(id);
    }

    @Override
    public List<Inquiry> findAllInquiry() {
        return inquiryRepositoryImpl.findAllInquiry();
    }

    @Override
    public void delete(Long id) {
        inquiryRepositoryImpl.delete(id);
    }
}
