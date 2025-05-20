package com.blog.conduit.controllers;

import com.blog.conduit.dtos.TagResponseDto;
import com.blog.conduit.models.ResponseObject;
import com.blog.conduit.models.Tag;
import com.blog.conduit.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    @Autowired
    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    // 1. Lấy danh sách tất cả tag
    @GetMapping
    public List<TagResponseDto> getAll() {
        return tagService.findAll();
    }

    // 2. Lấy chi tiết 1 tag theo id
    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject> getById(@PathVariable Integer id) {
        Optional<Tag> foundTag = tagService.findById(id);
        return foundTag.isPresent() ? ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "Query product successfully", foundTag)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("fasle", "Cannot find product with id = " + id, ""));
    }

    // 3. Tạo mới tag
//    @PostMapping
//    public ResponseEntity<ResponseObject> create(@RequestBody Tag tag) {
//        Optional<Tag> foundTag = tagService.findByName(tag.getTagName());
//        if (foundTag.isEmpty())
//            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject("OK", "tag inserted", tagService.create(tag)));
//        else
//            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body((new ResponseObject("failed", "tag already existed", "")));
//    }

    // 4. Cập nhật tag
//    @PutMapping("/{id}")
//    public ResponseEntity<Tag> update(@PathVariable Integer id, @RequestBody Tag tag) {
//        try {
//            Tag updated = tagService.update(id, tag);
//            return ResponseEntity.ok(updated);
//        } catch (RuntimeException ex) {
//            return ResponseEntity.notFound().build();
//        }
//    }

    // 5. Xóa tag
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Integer id) {
//        try {
//            tagService.delete(id);
//            return ResponseEntity.noContent().build();
//        } catch (RuntimeException ex) {
//            return ResponseEntity.notFound().build();
//        }
//    }
}
