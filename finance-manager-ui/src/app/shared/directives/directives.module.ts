import { NgModule } from '@angular/core';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';
import { SortableDirective } from './sortable.directive';

@NgModule({
  declarations: [
    SortableDirective
  ],
  imports: [
    CommonModule,
    NgbModule
  ],
  exports: [
    SortableDirective
  ]
})
export class DirectivesModule { }
