package com.thiagosalvess.admin.catalogy.infrastructure.castmember.models;

import com.thiagosalvess.admin.catalogy.domain.castmember.CastMemberType;

public record UpdateCastMemberRequest(String name, CastMemberType type) {}
