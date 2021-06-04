import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEnderecoPaciente, getEnderecoPacienteIdentifier } from '../endereco-paciente.model';

export type EntityResponseType = HttpResponse<IEnderecoPaciente>;
export type EntityArrayResponseType = HttpResponse<IEnderecoPaciente[]>;

@Injectable({ providedIn: 'root' })
export class EnderecoPacienteService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/endereco-pacientes');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(enderecoPaciente: IEnderecoPaciente): Observable<EntityResponseType> {
    return this.http.post<IEnderecoPaciente>(this.resourceUrl, enderecoPaciente, { observe: 'response' });
  }

  update(enderecoPaciente: IEnderecoPaciente): Observable<EntityResponseType> {
    return this.http.put<IEnderecoPaciente>(
      `${this.resourceUrl}/${getEnderecoPacienteIdentifier(enderecoPaciente) as number}`,
      enderecoPaciente,
      { observe: 'response' }
    );
  }

  partialUpdate(enderecoPaciente: IEnderecoPaciente): Observable<EntityResponseType> {
    return this.http.patch<IEnderecoPaciente>(
      `${this.resourceUrl}/${getEnderecoPacienteIdentifier(enderecoPaciente) as number}`,
      enderecoPaciente,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEnderecoPaciente>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEnderecoPaciente[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEnderecoPacienteToCollectionIfMissing(
    enderecoPacienteCollection: IEnderecoPaciente[],
    ...enderecoPacientesToCheck: (IEnderecoPaciente | null | undefined)[]
  ): IEnderecoPaciente[] {
    const enderecoPacientes: IEnderecoPaciente[] = enderecoPacientesToCheck.filter(isPresent);
    if (enderecoPacientes.length > 0) {
      const enderecoPacienteCollectionIdentifiers = enderecoPacienteCollection.map(
        enderecoPacienteItem => getEnderecoPacienteIdentifier(enderecoPacienteItem)!
      );
      const enderecoPacientesToAdd = enderecoPacientes.filter(enderecoPacienteItem => {
        const enderecoPacienteIdentifier = getEnderecoPacienteIdentifier(enderecoPacienteItem);
        if (enderecoPacienteIdentifier == null || enderecoPacienteCollectionIdentifiers.includes(enderecoPacienteIdentifier)) {
          return false;
        }
        enderecoPacienteCollectionIdentifiers.push(enderecoPacienteIdentifier);
        return true;
      });
      return [...enderecoPacientesToAdd, ...enderecoPacienteCollection];
    }
    return enderecoPacienteCollection;
  }
}
