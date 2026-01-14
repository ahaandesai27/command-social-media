import { Component } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import { RegisterSerivce } from '../../../services/auth/register.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {

  constructor(private registerService: RegisterSerivce, private router: Router) { }

  form = new FormGroup(
    {
      username: new FormControl('', [
        Validators.required,
        Validators.minLength(4)
      ]),
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
        Validators.maxLength(20)
      ]),
      confirmPassword: new FormControl('', [
        Validators.required
      ]),
    },
  );

  submit(): void {
    this.form.markAllAsTouched();
    if (this.form.invalid) return;

    const { username, password, confirmPassword } = this.form.value;
    if (password !== confirmPassword) return;

    this.registerService
      .register({ username: username!, password: password! })
      .subscribe({
        next: () => {
          this.router.navigate(['/login']);
        },
        error: (err) => {
          console.log(err)
          const message =
            err?.error?.message ||
            err?.error ||
            'Registration failed';

          alert(message);
        }
      });
  }

}
