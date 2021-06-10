package org.ksens.demo.java.springboot.clothstore.services.interfaces;

import org.ksens.demo.java.springboot.clothstore.models.ResponseModel;
import org.ksens.demo.java.springboot.clothstore.models.SizeModel;

public interface ISizeService {
    ResponseModel createSize(SizeModel sizeModel);
    ResponseModel updateSize(SizeModel sizeModel);
    ResponseModel getSizes();
    ResponseModel deleteSize(Long id);
}
