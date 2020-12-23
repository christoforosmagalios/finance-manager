import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AddressDTO } from 'src/app/dto/address-dto';
import { LoaderService } from 'src/app/shared/components/loader/loader.service';
import { Constants } from 'src/app/shared/constants/constants';
import { CRUDService } from 'src/app/shared/services/crud.service';

@Component({
  selector: 'app-address-item',
  templateUrl: './address-item.component.html',
  styleUrls: ['./address-item.component.css']
})
export class AddressItemComponent implements OnInit {
  // Address given as an input.
  @Input() address: AddressDTO;
  // Event to be sent to the parent, in order to update the address list.
  @Output() updateAddresses: EventEmitter<string> = new EventEmitter<string>();
  // Copy of the initial given address.
  addressOriginal: AddressDTO;

  constructor(
    private crud: CRUDService,
    private loader: LoaderService
  ) { }

  ngOnInit(): void {
    this.addressOriginal = {...this.address};
  }

  /**
   * Delete the address.
   */
  delete() {
    // Show the loader.
    this.loader.show();
    this.crud.delete(Constants.ENTITY.ADDRESS, this.address.id).subscribe(res => {
      // Hide the loader.
      this.loader.hide();
      // Send event to the parent in order to reload the address list.
      this.updateAddresses.emit();
    });
  }

  /**
   * Save the address.
   */
  save() {
    // Show the loader.
    this.loader.show();
    this.crud.save(Constants.ENTITY.ADDRESS, this.address).subscribe(res => {
      // Hide the loader.
      this.loader.hide();
      // Send event to the parent in order to reload the address list.
      this.updateAddresses.emit();
    });
  }

  /**
   * Cancel the edit mode, or the new addition.
   */
  cancel() {
    if (this.addressOriginal.id) {
      this.address = {...this.addressOriginal};
    } else {
      // Send event to the parent in order to reload the address list.
      this.updateAddresses.emit();
    }
    
  }

}
