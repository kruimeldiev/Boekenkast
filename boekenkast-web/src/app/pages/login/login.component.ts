import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthenticateUserRequest } from '../../services/models';
import { Router } from '@angular/router';
import { AuthenticationService } from '../../services/services/authentication.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  imports: [CommonModule, FormsModule]
})
export class LoginComponent {

  authRequest: AuthenticateUserRequest = {
    email: '',
    password: '',
  };
  errorMessages: Array<string> = [];

  constructor
    (
      private router: Router,
      private authService: AuthenticationService,
    ) {

  }

  authenticateUser() {
    this.errorMessages = [];
    console.log('Auth Request:', this.authRequest); // Debugging log
    this.authService.authenticateUser({ body: this.authRequest }).subscribe({
      next: (response) => {
        if (response) {
          // TODO: Store the token in local storage or a service
          this.router.navigate(['/home']);
        } else {
          // TODO: Handle the case when the response is null or undefined
          this.errorMessages.push('Unexpected error occurred. Please try again.');
        }
      },
      error: (error) => {
        // TODO: Handle the error response here
        console.error('Error during authentication:', error);
        if (error.error.validationErrors) {
          this.errorMessages = error.error.validationErrors;
        } else {
          this.errorMessages.push('Unexpected error occurred (2). Please try again.');
        }
      }
    });
  }

  navigateToSignUpPage() {
    this.router.navigate(['/signUp']);
  }
}
