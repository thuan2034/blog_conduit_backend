package com.blog.conduit.services;

import com.blog.conduit.dtos.TagResponseDto;
import com.blog.conduit.models.Tag;
import com.blog.conduit.repositories.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    private final TagRepository tagRepo;

    public TagService(TagRepository tagRepo) {
        this.tagRepo = tagRepo;
    }

    @Transactional(readOnly = true)
    public List<TagResponseDto> findAll() {
        return tagRepo.findAll().stream().map(tag->new TagResponseDto(tag.getTagName())).toList();
    }

    @Transactional(readOnly = true)
    public Optional<Tag> findById(Integer id) {
        return tagRepo.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Tag> findByName(String tagName){
        return tagRepo.findByTagName(tagName);
    }
    @Transactional
    public Tag create(String tagName) {

        return tagRepo.save(new Tag(tagName));
    }

    @Transactional
    public Tag update(Integer id, Tag newData) {
        return tagRepo.findById(id)
                .map(tag -> {
                    tag.setTagName(newData.getTagName());
                    return tagRepo.save(tag);
                })
                .orElseThrow(() -> new RuntimeException("Tag không tồn tại với id = " + id));
    }

    @Transactional
    public void delete(Integer id) {
        if (!tagRepo.existsById(id)) {
            throw new RuntimeException("Tag không tồn tại với id = " + id);
        }
        tagRepo.deleteById(id);
    }
}
