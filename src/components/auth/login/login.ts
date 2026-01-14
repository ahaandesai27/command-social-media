import { Component } from '@angular/core';
import {
  FormControl
  , FormGroup
  , ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../../services/auth/login.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, CommonModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
  standalone: true
})
export class Login {
  isLoading = false;

  constructor(
    private auth: AuthService,
    private router: Router
  ) { }

  form = new FormGroup({
    username: new FormControl('', [Validators.required]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(8),
      Validators.maxLength(20)
    ])
  });

  submit() {
    if (this.form.invalid) {
      alert("Invalid Data!")
      return;
    }

    this.isLoading = true;
    const { username, password } = this.form.value;
    console.log(username, password)

    this.auth.login({ username: username!, password: password! })
      .subscribe({
        next: () => {
          this.isLoading = false;
          this.router.navigate(['/profile']);
        },
        error: (err) => {
          this.isLoading = false;

          const message =
            err?.error?.message ||
            err?.error ||
            'Login failed';

          alert(message);
        }
      });

  }

}
