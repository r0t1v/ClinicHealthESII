import { IExame } from 'app/entities/exame/exame.model';

export interface IPagamentoExame {
  id?: number;
  formapagamento?: string | null;
  conteudo?: string | null;
  seliquidado?: boolean | null;
  exame?: IExame | null;
}

export class PagamentoExame implements IPagamentoExame {
  constructor(
    public id?: number,
    public formapagamento?: string | null,
    public conteudo?: string | null,
    public seliquidado?: boolean | null,
    public exame?: IExame | null
  ) {
    this.seliquidado = this.seliquidado ?? false;
  }
}

export function getPagamentoExameIdentifier(pagamentoExame: IPagamentoExame): number | undefined {
  return pagamentoExame.id;
}
