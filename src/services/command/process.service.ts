import { Injectable } from '@angular/core';
import { AuthService } from '../auth/login.service';
import { PostService } from '../post.service';
import { JwtService } from '../auth/jwt.service';
import { UserService } from '../user/user.service';
import { User } from '../../models/User';
import { SearchService } from '../search.service';
import { Observable } from 'rxjs';
import { FollowService } from '../user/follow.service';
import { RegisterSerivce } from '../auth/register.service';

type CommandResult =
    | { type: 'navigation'; path: string }
    | { type: 'action'; name: string; payload?: unknown, action$?: Observable<any> }
    | { type: 'error'; message: string; code: number }
    | { type: 'log'; content: string};


@Injectable({
    providedIn: 'root',
})
export class CommandProcessService {
    constructor(private authService: AuthService,
        private registerService: RegisterSerivce,
        private postService: PostService,
        private jwtService: JwtService,
        private userService: UserService,
        private searchService: SearchService,
        private followService: FollowService
    ) { }

    private readonly navigationMap: Record<string, string> = {
        home: '/',
        profile: '/profile',
        login: '/login',
        register: '/register',
        posts: '/posts',
        'create post': '/posts/create',
        docs: '/docs',
        documentation: '/docs',
        help: '/docs',
    };

    processCommand(command: string): CommandResult {
        const cmd = command.trim();

        if (cmd.startsWith('go ')) {
            return this.handleNavigation(cmd.slice(3).trim());
        }

        else {
            return this.handleAction(cmd.trim());
        }
    }

    private handleNavigation(arg: string): CommandResult {
        if (arg.startsWith('profile ')) {
            const username = arg.split(/\s+/)[1];
            return { type: 'navigation', path: `/profile/${username}` };
        }

        const path = this.navigationMap[arg];
        if (path) {
            return { type: 'navigation', path };
        }

        return { type: 'error', message: "Route not found!", code: 404 };
    }

    private handleAction(arg: string): CommandResult {
        console.log(arg)
        if (arg === 'logout') {
            this.authService.logout();
            return { type: 'action', name: 'logout' };
        }

        if (arg == "current user") {
          return {
            type: 'log',
            content: this.jwtService.getUsername() ?? "No user is logged in."
          }
        }

        if (arg.startsWith("login")) return this.loginViaCommand(arg);

        else if (arg.startsWith("register")) return this.registerViaCommand(arg);

        else if (arg.startsWith("search")) return this.searchViaCommand(arg);

        else if (arg.startsWith("create post")) return this.createPostViaCommand(arg);

        else if (arg.startsWith("edit")) return this.editProfileViaCommand(arg);

        else if (arg.startsWith("follow")) return this.followUser(arg);

        else if (arg.startsWith("unfollow")) return this.unfollowUser(arg);

        else if (arg === "help" || arg === "commands") {
            return {
                type: 'log',
                content: `Available commands:
Navigation: go home, go profile, go login, go register, go posts, go docs
Actions: logout, current user, login, search users/posts, create post, follow, unfollow
Profile: edit name, edit title, edit about, edit location, edit techStack
Help: help, commands, go docs (for full documentation)`
            };
        }

        return { type: 'error', message: "Action does not exist!", code: 404 };
    }

    // Helpers for action commands -> provides parsing as well as calls the related service  

    private loginViaCommand(input: string): CommandResult {
        const usernameMatch = input.match(/--username\s+([^\s]+)/);
        if (!usernameMatch) {
            return { type: 'error', message: 'Missing --username', code: 400 };
        }

        const passwordMatch = input.match(/--password\s+([^\s]+)/);
        if (!passwordMatch) {
            return { type: 'error', message: 'Missing --password', code: 400 };
        }

        const username = usernameMatch[1];
        const password = passwordMatch[1];

        return {
            type: 'action',
            name: 'login',
            action$: this.authService.login({ username, password })
        };
    }

    private registerViaCommand(input: string): CommandResult {
        const usernameMatch = input.match(/--username\s+([^\s]+)/);
        if (!usernameMatch) {
            return { type: 'error', message: 'Missing --username', code: 400 };
        }

        const passwordMatch = input.match(/--password\s+([^\s]+)/);
        if (!passwordMatch) {
            return { type: 'error', message: 'Missing --password', code: 400 };
        }

        const username = usernameMatch[1];
        const password = passwordMatch[1];

        return {
            type: 'action',
            name: 'register',
            action$: this.registerService.register({ username, password })
        };
        
    }
    private createPostViaCommand(input: string): CommandResult {
        // Handles the create post method, by showing if the user has missed some fields 
        // ! Add a helper showing the commmand syntax if the user fails 
        const titleMatch = input.match(/--title\s+"([^"]+)"/);
        if (!titleMatch) {
            return { type: 'error', message: 'Missing or invalid --title. Maybe you forgot to add quotes?', code: 400 };
        }

        const descriptionMatch = input.match(/--description\s+"([^"]+)"/);
        if (!descriptionMatch) {
            return { type: 'error', message: 'Missing or invalid --description. Maybe you forgot to add quotes?', code: 400 };
        }

        const title = titleMatch[1];
        const description = descriptionMatch[1];

        const username = this.jwtService.getUsername();
        if (!username) {
            return { type: 'error', message: 'User not logged in!', code: 403 };
        }

        return {
            type: 'action',
            name: 'post',
            action$: this.postService.createPost(title, description, username)
        };
    }

    private editProfileViaCommand(input: string): CommandResult {
        const normalized = input.trim().replace(/\s+/g, ' ');

        if (!normalized.startsWith('edit ')) {
            return { type: 'error', message: 'Invalid edit command', code: 400 };
        }

        const valueMatch = normalized.match(/"([^"]+)"/);
        if (!valueMatch) {
            return { type: 'error', message: 'Missing quoted value', code: 400 };
        }

        const value = valueMatch[1];
        const payload: Partial<User> = {};

        if (normalized.startsWith('edit name ')) {
            payload.name = value;
        } else if (normalized.startsWith('edit title ')) {
            payload.title = value;
        } else if (normalized.startsWith('edit about ')) {
            payload.about = value;
        } else if (normalized.startsWith('edit location ')) {
            payload.location = value;
        } else if (normalized.startsWith('edit techStack ')) {
            const stack = value
                .split(',')
                .map(v => v.trim())
                .filter(Boolean);

            if (!stack.length) {
                return { type: 'error', message: 'techStack cannot be empty', code: 400 };
            }

            payload.techStack = stack;
        } else {
            return { type: 'error', message: 'Unsupported edit field', code: 400 };
        }

        const userId = this.jwtService.getId();
        if (!userId) {
            return { type: 'error', message: 'User not logged in!', code: 403 };
        }

        return {
            type: 'action',
            name: 'editProfile',
            action$: this.userService.updateUser(userId, payload)
        };
    }

    private searchViaCommand(input: string): CommandResult {
        const parts = input.split(" ");

        if (parts[1] == "users") {
            if (parts.length == 2) {
                return { type: 'error', message: 'Cannot search with empty argument', code: 400 };
            }

            return {
                type: 'action',
                name: 'searchUsers',
                action$: this.searchService.searchUsers(parts[2], 5)
                // can set results in text or something - do later 
            };
        }
        else if (parts[1] == "posts") {
            if (parts.length == 2) {
                return { type: 'error', message: 'Cannot search with empty argument', code: 400 };
            }

            return {
                type: 'action',
                name: 'searchPosts',
                action$: this.searchService.searchPosts(parts[2], 0, 5)
            };
        }
        else {
            return { type: 'error', message: 'Invalid search parameter. Use users or posts.', code: 400 };
        }
    }

    private followUser(input: string): CommandResult {
        // for single user only 
        const parts: string[] = input.split(" ")
        if (parts.length != 2) {
            return { type: 'error', message: 'Username is not provided', code: 400 };
        }

        const toFollow = parts[1];
        const currentUsername = this.jwtService.getUsername();

        if (!currentUsername) {
            return { type: 'error', message: 'User not logged in!', code: 400 }
        }

        return {
            type: 'action',
            name: 'followUser',
            action$: this.followService.followUser(
                {
                    follower: currentUsername,
                    followee: toFollow,
                }
            )
        }
    }

    private unfollowUser(input: string): CommandResult {
        const parts: string[] = input.split(" ");
        if (parts.length !== 2) {
            return { type: 'error', message: 'Username is not provided', code: 400 };
        }

        const toUnfollow = parts[1];
        const currentUsername = this.jwtService.getUsername();

        if (!currentUsername) {
            return { type: 'error', message: 'User not logged in!', code: 400 };
        }

        return {
            type: 'action',
            name: 'unfollowUser',
            action$: this.followService.unfollowUser({
                follower: currentUsername,
                followee: toUnfollow,
            })
        };
    }

}
