package com.github.cmag.financemanager.controller;

import com.github.cmag.financemanager.dto.master.data.BillCategoryDTO;
import com.github.cmag.financemanager.model.master.data.BillCategory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Bill Category Rest controller.
 */
@RestController
@RequestMapping("/billCategory")
public class BillCategoryController extends BaseController<BillCategoryDTO, BillCategory> {

}
