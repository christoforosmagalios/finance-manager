import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-amount-card',
  templateUrl: './amount-card.component.html',
  styleUrls: ['./amount-card.component.css']
})
export class AmountCardComponent implements OnInit {
  // The header.
  @Input() heading: string;
  // The icon.
  @Input() icon: string;
  // The type.
  @Input() type: string;
  // The amount.
  @Input() amount: number;

  constructor() { }

  ngOnInit(): void {
  }

}
