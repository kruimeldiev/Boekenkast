/* tslint:disable */
/* eslint-disable */
/* Code generated by ng-openapi-gen DO NOT EDIT. */

import { HttpClient, HttpContext } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { BaseService } from '../base-service';
import { ApiConfiguration } from '../api-configuration';
import { StrictHttpResponse } from '../strict-http-response';

import { activateAccount } from '../fn/authentication/activate-account';
import { ActivateAccount$Params } from '../fn/authentication/activate-account';
import { authenticateUser } from '../fn/authentication/authenticate-user';
import { AuthenticateUser$Params } from '../fn/authentication/authenticate-user';
import { AuthenticateUserResponse } from '../models/authenticate-user-response';
import { registerUser } from '../fn/authentication/register-user';
import { RegisterUser$Params } from '../fn/authentication/register-user';


/**
 * Authentication endpoints
 */
@Injectable({ providedIn: 'root' })
export class AuthenticationService extends BaseService {
  constructor(config: ApiConfiguration, http: HttpClient) {
    super(config, http);
  }

  /** Path part for operation `registerUser()` */
  static readonly RegisterUserPath = '/auth/register';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `registerUser()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  registerUser$Response(params?: RegisterUser$Params, context?: HttpContext): Observable<StrictHttpResponse<{
}>> {
    return registerUser(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `registerUser$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  registerUser(params?: RegisterUser$Params, context?: HttpContext): Observable<{
}> {
    return this.registerUser$Response(params, context).pipe(
      map((r: StrictHttpResponse<{
}>): {
} => r.body)
    );
  }

  /** Path part for operation `authenticateUser()` */
  static readonly AuthenticateUserPath = '/auth/authenticate';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `authenticateUser()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  authenticateUser$Response(params?: AuthenticateUser$Params, context?: HttpContext): Observable<StrictHttpResponse<AuthenticateUserResponse>> {
    return authenticateUser(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `authenticateUser$Response()` instead.
   *
   * This method sends `application/json` and handles request body of type `application/json`.
   */
  authenticateUser(params?: AuthenticateUser$Params, context?: HttpContext): Observable<AuthenticateUserResponse> {
    return this.authenticateUser$Response(params, context).pipe(
      map((r: StrictHttpResponse<AuthenticateUserResponse>): AuthenticateUserResponse => r.body)
    );
  }

  /** Path part for operation `activateAccount()` */
  static readonly ActivateAccountPath = '/auth/activate-account';

  /**
   * This method provides access to the full `HttpResponse`, allowing access to response headers.
   * To access only the response body, use `activateAccount()` instead.
   *
   * This method doesn't expect any request body.
   */
  activateAccount$Response(params: ActivateAccount$Params, context?: HttpContext): Observable<StrictHttpResponse<void>> {
    return activateAccount(this.http, this.rootUrl, params, context);
  }

  /**
   * This method provides access only to the response body.
   * To access the full response (for headers, for example), `activateAccount$Response()` instead.
   *
   * This method doesn't expect any request body.
   */
  activateAccount(params: ActivateAccount$Params, context?: HttpContext): Observable<void> {
    return this.activateAccount$Response(params, context).pipe(
      map((r: StrictHttpResponse<void>): void => r.body)
    );
  }

}
