package com.blog.conduit.services;

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
    public List<Tag> findAll() {
        return tagRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Tag> findById(Integer id) {
        return tagRepo.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Tag> findByTagName(String tagName){
        return tagRepo.findByTagName(tagName);
    }
    @Transactional
    public Tag create(Tag tag) {
        return tagRepo.save(tag);
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
