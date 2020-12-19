package com.github.cmag.financemanager.controller;

import com.github.cmag.financemanager.dto.AddressDTO;
import com.github.cmag.financemanager.model.Address;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Address Rest controller.
 */
@RestController
@RequestMapping("/address")
public class AddressController extends BaseController<AddressDTO, Address> {

}
