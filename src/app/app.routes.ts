import { Routes } from '@angular/router';
import { Profile } from '../components/profile/profile';
import { Homepage } from '../components/homepage/homepage';
import { NotFound } from '../components/not-found/not-found';
import { Login } from '../components/auth/login/login';
import { Register } from '../components/auth/register/register';
import { EditProfile } from '../components/edit-profile/edit-profile';
import { Posts } from '../components/posts/posts';
import { Search } from '../components/search/search';
import { CreatePost } from '../components/create-post/create-post';

export const routes: Routes = [
    // post 
    {path: 'posts/create', component: CreatePost},
    {path: 'posts', component: Posts},

    // search 
    {path: 'search', component: Search},

    // profile page 
    {path: 'edit-profile', component: EditProfile},
    {path: 'profile/:username', component: Profile},
    {path: 'profile', component: Profile},

    // auth
    {path: 'login', component: Login},
    {path: 'register', component: Register},


    {path: '', component: Homepage},
    
    {path: '**', component: NotFound} // Wildcard route for 404 page
];
