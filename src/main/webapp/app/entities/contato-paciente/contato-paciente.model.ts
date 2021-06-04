import { IPaciente } from 'app/entities/paciente/paciente.model';

export interface IContatoPaciente {
  id?: number;
  tipo?: string | null;
  conteudo?: string | null;
  paciente?: IPaciente | null;
}

export class ContatoPaciente implements IContatoPaciente {
  constructor(public id?: number, public tipo?: string | null, public conteudo?: string | null, public paciente?: IPaciente | null) {}
}

export function getContatoPacienteIdentifier(contatoPaciente: IContatoPaciente): number | undefined {
  return contatoPaciente.id;
}
