package org.asadorian.timetabling.rest;

import org.asadorian.timetabling.domain.Lesson;
import org.asadorian.timetabling.domain.Timeslot;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/timeslots")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public class TimeslotResource {

    @POST
    public Response add(Timeslot timeslot) {
        Timeslot.persist(timeslot);
        return Response.accepted(timeslot).build();
    }

    @DELETE
    @Path("{timeslotId}")
    public Response delete(@PathParam("timeslotId") Long timeslotId) {
        Timeslot timeslot = Lesson.findById(timeslotId);
        if (timeslot == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        timeslot.delete();
        return Response.status(Response.Status.OK).build();
    }
}
