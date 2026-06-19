package com.example.library_management.service;

import com.example.library_management.dto.ReaderCreateDTO;
import com.example.library_management.dto.ReaderUpdateDTO;
import com.example.library_management.entity.Reader;
import com.example.library_management.exception.DuplicateEmailException;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ReaderService {

    private final ReaderRepository readerRepository;
    private final CloudinaryService cloudinaryService;

    @Autowired
    public ReaderService(ReaderRepository readerRepository, CloudinaryService cloudinaryService) {
        this.readerRepository = readerRepository;
        this.cloudinaryService = cloudinaryService;
    }

    public Reader createReader(ReaderCreateDTO dto) {
        if (readerRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEmailException("Email already exists: " + dto.getEmail());
        }

        String avatarUrl = null;
        MultipartFile file = dto.getAvatarImage();
        if (file != null && !file.isEmpty()) {
            avatarUrl = cloudinaryService.uploadImage(file);
        }

        Reader reader = new Reader();
        reader.setName(dto.getName());
        reader.setEmail(dto.getEmail());
        reader.setPhone(dto.getPhone());
        reader.setAddress(dto.getAddress());
        reader.setAvatarUrl(avatarUrl);

        return readerRepository.save(reader);
    }

    public Reader getReaderById(Long id) {
        return readerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reader not found with id: " + id));
    }

    public List<Reader> getAllReaders() {
        return readerRepository.findAll();
    }

    public Reader updateReader(Long id, ReaderUpdateDTO dto) {
        Reader reader = getReaderById(id);

        if (dto.getName() != null) {
            reader.setName(dto.getName());
        }
        if (dto.getEmail() != null) {
            if (!dto.getEmail().equals(reader.getEmail()) && readerRepository.existsByEmail(dto.getEmail())) {
                throw new DuplicateEmailException("Email already exists: " + dto.getEmail());
            }
            reader.setEmail(dto.getEmail());
        }
        if (dto.getPhone() != null) {
            reader.setPhone(dto.getPhone());
        }
        if (dto.getAddress() != null) {
            reader.setAddress(dto.getAddress());
        }

        MultipartFile file = dto.getAvatarImage();
        if (file != null && !file.isEmpty()) {
            reader.setAvatarUrl(cloudinaryService.uploadImage(file));
        }

        return readerRepository.save(reader);
    }

    public void deleteReader(Long id) {
        Reader reader = getReaderById(id);
        readerRepository.delete(reader);
    }
}
