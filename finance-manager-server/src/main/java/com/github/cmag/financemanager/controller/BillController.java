package com.github.cmag.financemanager.controller;

import com.github.cmag.financemanager.dto.BillDTO;
import com.github.cmag.financemanager.model.Bill;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Bill Rest controller.
 */
@RestController
@RequestMapping("/bill")
public class BillController extends BaseController<BillDTO, Bill> {

}
