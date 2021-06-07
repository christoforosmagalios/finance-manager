import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { ResetPasswordGuard } from "../shared/guards/reset-password.guard";
import { ForgotPasswordComponent } from "./forgot-password/forgot-password.component";
import { LoginComponent } from "./login/login.component";
import { RegisterComponent } from "./register/register.component";
import { ResetPasswordComponent } from "./reset-password/reset-password.component";

const authRoutes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'forgotPassword', component: ForgotPasswordComponent },
    { path: 'resetPassword/:id', component: ResetPasswordComponent, canActivate: [ResetPasswordGuard]}
];

@NgModule({
    imports: [RouterModule.forChild(authRoutes)],
    exports: [RouterModule]
  })
  export class AuthRoutingModule { }