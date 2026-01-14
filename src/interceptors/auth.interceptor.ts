import { HttpInterceptorFn } from "@angular/common/http";
import { inject } from "@angular/core";
import { AuthService } from "../services/auth/login.service";

export const authInterceptor: HttpInterceptorFn = (req, next) => {
    //? This is for my API requests
    //? It injects the bearer token if token is present
    //? otherwise sends without token, at which point the server will return a 403

    const token = inject(AuthService).getToken();
    
    if (!token) return next(req);

    return next(
        req.clone({
            setHeaders: {
                Authorization: `Bearer ${token}`
            }
        })
    )
}