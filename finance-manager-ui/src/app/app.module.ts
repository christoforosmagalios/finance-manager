import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { HttpClient, HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ConfirmationModalComponent } from './shared/components/confirmation-modal/confirmation-modal.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ToastrModule } from 'ngx-toastr';
import { HttpInterceptorService } from './shared/interceptor/http-interceptor.service';
import { LoaderComponent } from './shared/components/loader/loader.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { HeaderComponent } from './layout/header/header.component';
import { TransactionsModule } from './transactions/transactions.module';
import { TransactionsRoutingModule } from './transactions/transactions.routing.module';
import { TranslateLoader, TranslateModule } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { BillsModule } from './bills/bills.module';
import { BillsRoutingModule } from './bills/bills.routing.module';
import { ChartsModule } from 'ng2-charts';
import { PieChartComponent } from './shared/components/charts/pie-chart/pie-chart.component';
import { LineChartComponent } from './shared/components/charts/line-chart/line-chart.component';
import { AuthModule } from './auth/auth.module';
import { AuthRoutingModule } from './auth/auth.routing.module';

@NgModule({
  declarations: [
    AppComponent,
    ConfirmationModalComponent,
    LoaderComponent,
    DashboardComponent,
    HeaderComponent,
    PieChartComponent,
    LineChartComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    HttpClientModule,
    BrowserAnimationsModule,
    TransactionsRoutingModule,
    BillsRoutingModule,
    AuthRoutingModule,
    ToastrModule.forRoot(),
    TransactionsModule,
    BillsModule,
    AuthModule,
    ChartsModule,
    TranslateModule.forRoot({
      loader: {
      provide: TranslateLoader,
      useFactory: HttpLoaderFactory,
      deps: [HttpClient]
      }
    })
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpInterceptorService,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}
