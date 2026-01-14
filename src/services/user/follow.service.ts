import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment as env } from "../../env/env";

export interface FollowDto {
  followee: string;
  follower: string;
}

@Injectable({
  providedIn: 'root'
})
export class FollowService {
  private readonly API_URL = `${env.apiBaseUrl}/follow`;

  constructor(private http: HttpClient) {}

  followUser(followDto: FollowDto): Observable<{message: string}> {
    console.log("Follow DTO being sent", followDto);
    return this.http.post<{message: string}>(this.API_URL, followDto);
  }

  unfollowUser(followDto: FollowDto): Observable<{message: string}> {
    return this.http.delete<{message: string}>(this.API_URL, { body: followDto });
  }

  getFollowing(username: string): Observable<string[]> {
    return this.http.get<string[]>(`${this.API_URL}/following/${username}`);
  }

  getFollowers(username: string): Observable<string[]> {
    return this.http.get<string[]>(`${this.API_URL}/followers/${username}`);
  }
}