package org.project.shop.service;

import org.project.shop.repository.FileRepository;
import org.springframework.transaction.annotation.Transactional;

public class FileServiceImpl implements FileService{
    private FileRepository fileRepository;
    @Override
    @Transactional
    public Long saveFile(File file) {
        return fileRepository.save(file).getId();
    }

    @Override
    @Transactional
    public File getFile(Long id) {
        return fileRepository.findById(id).get();
    }
}
