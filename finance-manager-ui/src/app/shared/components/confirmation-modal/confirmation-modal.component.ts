import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-confirmation-modal',
  templateUrl: './confirmation-modal.component.html',
  styleUrls: ['./confirmation-modal.component.css']
})
export class ConfirmationModalComponent implements OnInit {

  // Modal title.
  title: string;
  // Modal content.
  description: string;

  constructor(public modal: NgbActiveModal) {}

  ngOnInit(): void {
  }

}
