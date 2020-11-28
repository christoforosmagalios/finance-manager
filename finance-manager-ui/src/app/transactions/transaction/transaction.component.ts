import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Params } from '@angular/router';
import { TransactionDTO } from 'src/app/dto/transaction-dto';
import { Location } from '@angular/common';

@Component({
  selector: 'app-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.css']
})
export class TransactionComponent implements OnInit {
  // The id of the transaction to be edited.
  id: string;
  // True if edit mode, false otherwise.
  editMode = false;
  // The transaction.
  transaction = new TransactionDTO();

  constructor(
    private route: ActivatedRoute,
    private location: Location
  ) { }

  ngOnInit(): void {
    // Get the params.
    this.route.params.subscribe((params: Params) => {
      this.id = params['id'];
      this.editMode = params['id'] == null;
      // Initialize the form.
      this.initForm();
    });
  }

  private initForm() {
    
  }

  /**
   * Submit the transaction form.
   * 
   * @param form The Form.
   */
  onSubmit(form: NgForm) {

  }

  /**
   * Navigate to previous state.
   */
  onCancel() {
    this.location.back();
  }

}
