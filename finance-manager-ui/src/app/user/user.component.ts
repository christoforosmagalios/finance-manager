import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { UserDetailsDTO } from '../dto/user-details-dto';
import { LoaderService } from '../shared/components/loader/loader.service';
import { UserService } from './user.service';
import { UtilsService } from '../shared/services/utils.service';
import { BillService } from '../bills/bill.service';
import { TransactionService } from '../transactions/transaction.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {
  // Edit mode flag.
  editMode = false;
  // User object.
  user = new UserDetailsDTO();
  // User original object.
  userOriginal = new UserDetailsDTO();
  // Error object.
  errors = {
    username: null,
    email: null,
    password: null,
    firstName: null,
    lastName: null
  };
  // Number of bills.
  bills: number = 0;
  // Number of transactions.
  transactions: number = 0;

  constructor(
    private userService: UserService,
    private loader: LoaderService,
    public utils: UtilsService,
    private billService: BillService,
    private transactionService: TransactionService) { }

  ngOnInit(): void {
    // Show Loader.
    this.loader.show();
    this.userService.findLoggedInUser().subscribe((user: UserDetailsDTO) => {
      this.initUserObjects(user);
      // Hide the loader.
      this.loader.hide();
    });

    this.billService.getTotalNumberOfBills().subscribe(count => {
      this.bills = count;
    });

    this.transactionService.getTotalNumberOfTransactions().subscribe(count => {
      this.transactions = count;
    });
  }

  /**
   * Submit the registration form.
   * 
   * @param form The Form.
   */
  onSubmit(form: NgForm) {
    // Clear Errors
    this.utils.clearErrors(this.errors);
    // Show Loader.
    this.loader.show();
    // Update the user details.
    this.userService.update(this.user).subscribe((user: UserDetailsDTO) => {
      // Hide the loader.
      this.loader.hide();
      // Update user objects.
      this.initUserObjects(user);
    }, error => {
      this.utils.markErrors(this.errors, error);
    });
  }

  /**
   * Initialize the user instances and the edit mode.
   * 
   * @param user The user object.
   */
  private initUserObjects(user: UserDetailsDTO) {
    this.editMode = false;
    this.user = user;
    this.userOriginal = {...this.user};
  }

  /**
   * Exit edit mode. Set the user object to the initial state 
   * and update the edit mode flag.
   */
  onAbort() {
    this.editMode = false;
    this.user = {...this.userOriginal};
  }

}
