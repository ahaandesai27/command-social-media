import { Component, OnInit } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { UserService } from '../../services/user/user.service';
import { UserProjectService } from '../../services/user/user-project.service';
import { JwtService } from '../../services/auth/jwt.service';
import { ProfileFormService } from '../../services/user/profile-form.service';
import { NotLoggedIn } from '../not-logged-in/not-logged-in';

@Component({
  selector: 'app-edit-profile',
  standalone: true,
  imports: [ReactiveFormsModule, NotLoggedIn],
  templateUrl: './edit-profile.html',
  styleUrl: './edit-profile.css'
})
export class EditProfile implements OnInit {
  isLoading = false;
  public username: string | null = null;
  public isAuthenticated: boolean = false;
  private userId: number | null = null;

  constructor(
    private userService: UserService,
    private userProjectService: UserProjectService,
    private jwtService: JwtService,
    public profileForm: ProfileFormService
  ) { }

  ngOnInit() {
    this.username = this.jwtService.getUsername();
    this.userId = this.jwtService.getId();
    this.isAuthenticated = !!this.username && !this.jwtService.isExpired();

    console.log("Current username: ", this.username);

    if (!this.username || !this.userId || !this.isAuthenticated) return;

    this.userService.getUserByUsername(this.username).subscribe(user =>
      this.profileForm.setUser(user)
    );
  }

  submit() {
    const form = this.profileForm.form;

    if (form.invalid) {
      form.markAllAsTouched();
      return;
    }

    if (!this.userId) return;

    this.isLoading = true;
    const payload = this.profileForm.getValue();

    this.userService.updateUser(this.userId, payload)
      .subscribe({
        next: () => {
          this.isLoading = false;
          alert('Successfully edited profile!');
        },
        error: () => {
          this.isLoading = false;
          alert('Failed to update profile');
        }
      });
  }

  saveProject(index: number) {
    const projectControl = this.profileForm.projects.at(index);

    if (!projectControl || projectControl.invalid) {
      projectControl?.markAllAsTouched();
      alert('Please fill in all required project fields');
      return;
    }

    if (!this.userId) {
      alert('User not authenticated');
      return;
    }

    const projectData = projectControl.value;
    console.log(index);
    
    this.userProjectService.addProject(this.userId, projectData)
      .subscribe({
        next: () => {
          alert(`Project "${projectData.name}" saved successfully!`);
        },
        error: (error) => {
          console.error('Error saving project:', error);
          alert('Failed to save project');
        }
      });
  }

  get canAddProject(): boolean {
    const projectCount = this.profileForm.projects.length;

    // Can't add if already at limit of 3
    if (projectCount >= 3) {
      return false;
    }

    // If no projects, can add
    if (projectCount === 0) {
      return true;
    }

    // Check if the last project is completely filled
    const lastProject = this.profileForm.projects.at(projectCount - 1);
    return lastProject ? lastProject.valid : false;
  }

  get shouldShowAddButton(): boolean {
    return this.profileForm.projects.length < 3;
  }

}
