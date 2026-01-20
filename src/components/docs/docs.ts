import { Component } from '@angular/core';

@Component({
  selector: 'app-docs',
  standalone: true,
  imports: [],
  templateUrl: './docs.html',
  styleUrls: ['./docs.css']
})
export class Docs {
  
  navigationCommands = [
    {
      command: 'go home',
      description: 'Navigate to the homepage',
      example: 'go home'
    },
    {
      command: 'go profile',
      description: 'Navigate to your profile page',
      example: 'go profile'
    },
    {
      command: 'go profile [username]',
      description: 'Navigate to a specific user\'s profile',
      example: 'go profile johndoe'
    },
    {
      command: 'go login',
      description: 'Navigate to the login page',
      example: 'go login'
    },
    {
      command: 'go register',
      description: 'Navigate to the registration page',
      example: 'go register'
    },
    {
      command: 'go posts',
      description: 'Navigate to the posts feed',
      example: 'go posts'
    },
    {
      command: 'go create post',
      description: 'Navigate to the create post page',
      example: 'go create post'
    }
  ];

  actionCommands = [
    {
      command: 'current user',
      description: 'Display the currently logged-in username',
      example: 'current user'
    },
    {
      command: 'login --username [user] --password [pass]',
      description: 'Log in with your credentials',
      example: 'login --username johndoe --password mypassword'
    },
    {
      command: 'search users [query]',
      description: 'Search for users by username or name',
      example: 'search users john'
    },
    {
      command: 'search posts [query]',
      description: 'Search for posts by content',
      example: 'search posts angular'
    },
    {
      command: 'create post --title "[title]" --description "[desc]"',
      description: 'Create a new post with title and description',
      example: 'create post --title "My First Post" --description "This is my first post on Command Social!"'
    },
    {
      command: 'follow [username]',
      description: 'Follow a user',
      example: 'follow johndoe'
    },
    {
      command: 'unfollow [username]',
      description: 'Unfollow a user',
      example: 'unfollow johndoe'
    },
    {
      command: 'logout',
      description: 'Log out of your account',
      example: 'logout'
    },
  ];

  editCommands = [
    {
      command: 'edit name "[value]"',
      description: 'Update your display name',
      example: 'edit name "John Doe"'
    },
    {
      command: 'edit title "[value]"',
      description: 'Update your professional title',
      example: 'edit title "Senior Software Engineer"'
    },
    {
      command: 'edit about "[value]"',
      description: 'Update your about/bio section',
      example: 'edit about "Passionate developer who loves Angular and TypeScript"'
    },
    {
      command: 'edit location "[value]"',
      description: 'Update your location',
      example: 'edit location "San Francisco, CA"'
    },
    {
      command: 'edit techStack "[tech1, tech2, ...]"',
      description: 'Update your technology stack (comma-separated)',
      example: 'edit techStack "Angular, TypeScript, Node.js, MongoDB"'
    }
  ];

  constructor() { }
}