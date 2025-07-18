package org.bsc.langgraph4j.state;

import org.bsc.langgraph4j.prebuilt.MessagesState;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.bsc.langgraph4j.utils.CollectionsUtils.mapOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AgentStateTest {

    static class State extends MessagesState<String> {

        public State(Map<String, Object> initData) {
            super( initData  );
        }

        int steps() {
            return this.<Integer>value("steps").orElse(0);
        }

    }

    public Map<String,Object> initDataFromSchema( Map<String,Channel<?>> schema )  {
        return  schema.entrySet().stream()
                .filter( c -> c.getValue().getDefault().isPresent() )
                .collect(Collectors.toMap(Map.Entry::getKey, e ->
                        e.getValue().getDefault().get().get()
                ));
    }

    @Test
    public void clearAppenderTest() {

        AgentStateFactory<State> sf = State::new;

        var schema = Map.<String,Channel<?>> of( "messages", Channels.appender(ArrayList::new) );
        var state = sf.applyFromSchema( schema );

        assertTrue( state.messages().isEmpty() );

        var newStateData = AgentState.updateState( state,
                Map.of( "messages", "item1"),
                schema);

        state = sf.apply( newStateData );

        assertEquals( 1, state.messages().size() );

        newStateData = AgentState.updateState( state,
                Map.of( "messages", "item2"),
                schema);

        state = sf.apply( newStateData );

        assertEquals( 2, state.messages().size() );

        newStateData = AgentState.updateState( state,
                mapOf( "messages", null),
                schema);

        state = sf.apply( newStateData );

        assertTrue( state.messages().isEmpty() );

    }

    @Test
    public void removeDataFromStateTest() {

        AgentStateFactory<AgentState> sf = AgentState::new;

        var state = new AgentState( new HashMap<>() );

        var data = AgentState.updateState( state, Map.of( "attr1", "test"), Map.of());

        assertEquals( 1, data.size() );
        assertEquals( "test", data.get("attr1") );

        data = AgentState.updateState( data, mapOf( "attr1", null), Map.of());

        assertEquals( 0, data.size() );

        data = AgentState.updateState( state, Map.of( "attr1", "test"), Map.of());

        assertEquals( 1, data.size() );
        assertEquals( "test", data.get("attr1") );

        data = AgentState.updateState( data, Map.of( "attr1", AgentState.MARK_FOR_RESET), Map.of());

        assertEquals( 0, data.size() );

    }

}
