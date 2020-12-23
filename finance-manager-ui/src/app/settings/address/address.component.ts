import { Component, OnInit } from '@angular/core';
import { AddressDTO } from 'src/app/dto/address-dto';
import { LoaderService } from 'src/app/shared/components/loader/loader.service';
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

  constructor(
    private crud: CRUDService,
    private loader: LoaderService
  ) { }

  ngOnInit(): void {
    // Find all the addresses.
    this.findAll();
  }

  /**
   * Find all the addresses.
   */
  findAll() {
    // Show the loader.
    this.loader.show();
    this.crud.findAll(Constants.ENTITY.ADDRESS).subscribe((res: AddressDTO[]) => {
      // Assign the result.
      this.addresses = res;
      // Hide the loader.
      this.loader.hide();
    });
  }

  /**
   * Add a new empty address to the array.
   */
  addAddress() {
    // Create new Address DTO.
    let address = new AddressDTO();
    // Enable the edit mode.
    address.enableEdit = true;
    // Add the new address at the beginning of the array.
    this.addresses.unshift(address);
  }
}
