import { Component, OnInit } from '@angular/core';
import { Observable, of } from 'rxjs';
import { catchError, debounceTime, distinctUntilChanged, switchMap, tap } from 'rxjs/operators';
import { SearchResultDTO } from 'src/app/dto/search-result-dto';
import { DataService } from 'src/app/settings/data/data.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {
  // The formatter defines the text that will be displayed in the 
  // search input when the user selects one of the options. In this 
  // case, we clear the search input field.
  formatter = (x: SearchResultDTO) => "";
  // True if a search is currently ongoing, false otherwise.
  searching = false;
  // True if a search request has failed, false otherwise.
  searchFailed = false;

  constructor(private dataService: DataService) { }

  ngOnInit(): void {
    // Do nothing.
  }

  /**
   * If the given text's length is grater than 3, search for Bills and Transactions
   * that contain the given text in their description or notes.
   * 
   * @param text$ The text wo search with.
   */
  search = (text$: Observable<string>) =>
    text$.pipe(
      debounceTime(400),
      distinctUntilChanged(),
      tap(() => this.searching = true),
      switchMap(term => term.length < 4 ? [] :
        this.dataService.search(term).pipe(
          tap(() => this.searchFailed = false),
          catchError(() => {
            this.searchFailed = true;
            return of([]);
          }))
      ),
      tap(() => this.searching = false)
    )

}
