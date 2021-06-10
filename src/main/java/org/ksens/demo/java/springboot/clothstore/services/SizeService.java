package org.ksens.demo.java.springboot.clothstore.services;

import org.ksens.demo.java.springboot.clothstore.entities.Category;
import org.ksens.demo.java.springboot.clothstore.entities.Size;
import org.ksens.demo.java.springboot.clothstore.models.CategoryModel;
import org.ksens.demo.java.springboot.clothstore.models.ResponseModel;
import org.ksens.demo.java.springboot.clothstore.models.SizeModel;
import org.ksens.demo.java.springboot.clothstore.repositories.SizeDao;
import org.ksens.demo.java.springboot.clothstore.services.interfaces.ISizeService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SizeService implements ISizeService {

    private final SizeDao sizeDao;

    public SizeService(SizeDao sizeDao) {
        this.sizeDao = sizeDao;
    }

    public ResponseModel createSize(SizeModel sizeModel) {
        Size size =
                Size.builder().title(sizeModel.getTitle().trim()).build();
        sizeDao.save(size);
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("Size %s Created", size.getTitle()))
                .build();
    }

    public ResponseModel updateSize(SizeModel sizeModel) {
        Size size =
                Size.builder()
                        .id(sizeModel.getId())
                        .title(sizeModel.getTitle())
                        .build();
        sizeDao.save(size);
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .message(String.format("Size %s Updated", size.getTitle()))
                .build();
    }

    public ResponseModel getSizes() {
        List<Size> sizes = sizeDao.findAll(Sort.by("id"));
        List<SizeModel> sizeModels =
                sizes.stream()
                        .map(c ->
                                SizeModel.builder()
                                        .id(c.getId())
                                        .title(c.getTitle())
                                        .build()
                        )
                        .collect(Collectors.toList());
        return ResponseModel.builder()
                .status(ResponseModel.SUCCESS_STATUS)
                .data(sizeModels)
                .build();
    }

    public ResponseModel deleteSize(Long id) {
        Optional<Size> sizeOptional = sizeDao.findById(id);
        if (sizeOptional.isPresent()){
            Size size = sizeOptional.get();
            sizeDao.delete(size);
            return ResponseModel.builder()
                    .status(ResponseModel.SUCCESS_STATUS)
                    .message(String.format("Size #%s Deleted", size.getTitle()))
                    .build();
        } else {
            return ResponseModel.builder()
                    .status(ResponseModel.FAIL_STATUS)
                    .message(String.format("Size #%d Not Found", id))
                    .build();
        }
    }
}
