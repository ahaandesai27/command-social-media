import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-not-logged-in',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './not-logged-in.html',
  styleUrl: './not-logged-in.css'
})
export class NotLoggedIn {
  
  constructor(private router: Router) {}

  navigateToLogin() {
    this.router.navigate(['/login']);
  }

  navigateToRegister() {
    this.router.navigate(['/register']);
  }
}