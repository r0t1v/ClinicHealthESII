import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPagamentoExame, getPagamentoExameIdentifier } from '../pagamento-exame.model';

export type EntityResponseType = HttpResponse<IPagamentoExame>;
export type EntityArrayResponseType = HttpResponse<IPagamentoExame[]>;

@Injectable({ providedIn: 'root' })
export class PagamentoExameService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/pagamento-exames');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(pagamentoExame: IPagamentoExame): Observable<EntityResponseType> {
    return this.http.post<IPagamentoExame>(this.resourceUrl, pagamentoExame, { observe: 'response' });
  }

  update(pagamentoExame: IPagamentoExame): Observable<EntityResponseType> {
    return this.http.put<IPagamentoExame>(`${this.resourceUrl}/${getPagamentoExameIdentifier(pagamentoExame) as number}`, pagamentoExame, {
      observe: 'response',
    });
  }

  partialUpdate(pagamentoExame: IPagamentoExame): Observable<EntityResponseType> {
    return this.http.patch<IPagamentoExame>(
      `${this.resourceUrl}/${getPagamentoExameIdentifier(pagamentoExame) as number}`,
      pagamentoExame,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPagamentoExame>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPagamentoExame[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPagamentoExameToCollectionIfMissing(
    pagamentoExameCollection: IPagamentoExame[],
    ...pagamentoExamesToCheck: (IPagamentoExame | null | undefined)[]
  ): IPagamentoExame[] {
    const pagamentoExames: IPagamentoExame[] = pagamentoExamesToCheck.filter(isPresent);
    if (pagamentoExames.length > 0) {
      const pagamentoExameCollectionIdentifiers = pagamentoExameCollection.map(
        pagamentoExameItem => getPagamentoExameIdentifier(pagamentoExameItem)!
      );
      const pagamentoExamesToAdd = pagamentoExames.filter(pagamentoExameItem => {
        const pagamentoExameIdentifier = getPagamentoExameIdentifier(pagamentoExameItem);
        if (pagamentoExameIdentifier == null || pagamentoExameCollectionIdentifiers.includes(pagamentoExameIdentifier)) {
          return false;
        }
        pagamentoExameCollectionIdentifiers.push(pagamentoExameIdentifier);
        return true;
      });
      return [...pagamentoExamesToAdd, ...pagamentoExameCollection];
    }
    return pagamentoExameCollection;
  }
}
