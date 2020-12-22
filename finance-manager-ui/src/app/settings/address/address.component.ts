import { Component, OnInit } from '@angular/core';
import { AddressDTO } from 'src/app/dto/address-dto';
import { Constants } from 'src/app/shared/constants/constants';
import { CRUDService } from 'src/app/shared/services/crud.service';

@Component({
  selector: 'app-address',
  templateUrl: './address.component.html',
  styleUrls: ['./address.component.css']
})
export class AddressComponent implements OnInit {
  // List of addresses.
  addresses: AddressDTO[];
  // List of addresses to be edited.
  addressesEditable: AddressDTO[];

  constructor(
    private crud: CRUDService
  ) { }

  ngOnInit(): void {
    this.crud.findAll(Constants.ENTITY.ADDRESS).subscribe((res: AddressDTO[]) => {
      this.addresses = res;
      this.addressesEditable = res;
    });
  }

  delete(id: string) {
    console.log(id);
  }

  save(address: AddressDTO) {
    console.log(address);
  }

}
