import { Component, OnInit } from '@angular/core';
import { LoaderService } from 'src/app/shared/components/loader/loader.service';
import { UtilsService } from 'src/app/shared/services/utils.service';
import { DataService } from './data.service';

@Component({
  selector: 'app-data',
  templateUrl: './data.component.html'
})
export class DataComponent implements OnInit {

  constructor(
    private dataService: DataService,
    private loader: LoaderService,
    private utils: UtilsService) { }

  ngOnInit(): void {
    // Do nothing.
  }

  reindex() {
    this.loader.show();
    this.dataService.reindex().subscribe(res => {
      this.loader.hide();
      this.utils.showInfo("data.sync.completed");
    });
  }

}
